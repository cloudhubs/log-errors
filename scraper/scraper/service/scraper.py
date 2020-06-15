from __future__ import print_function
# To set the http_proxy environment variable
import os

# For making the site requests and overall request management
from scraper.service.stack_overflow import StackOverflow
# For child link queue
from queue import Queue
# For threading the scraping process
import threading
# For serialization
import json
# For thread management
from scraper.service.thread_executioner import ThreadExecutioner
import inspect


class StackOversight(object):

    def __init__(self, client_keys: list, proxy=None):
        print("Function -> '{}'\t\t".format(inspect.currentframe().f_code.co_name) + " Thread -> " + str(
            threading.get_ident()))

        if proxy:
            # address of the proxy server
            self.proxy = 'http://localhost:5050'

            # set the environment variable http_proxy and such
            os.environ['http_proxy'] = proxy
            os.environ['HTTP_PROXY'] = proxy
            os.environ['https_proxy'] = proxy
            os.environ['HTTPS_PROXY'] = proxy

        # self.site = StackOverflow()
        self.site = StackOverflow(client_keys)

        self.thread_handles = []
        self.file_handles = []

        self.code_lock = threading.Lock()
        self.text_lock = threading.Lock()

    def start(self, parent_link_queue: Queue, code_file_name='code.txt', text_file_name='text.txt'):
        print("Function -> '{}'\t\t".format(inspect.currentframe().f_code.co_name) + " Thread -> " + str(
            threading.get_ident()))
        code_io_handle = open(code_file_name, 'w')
        text_io_handle = open(text_file_name, 'w')

        self.file_handles.extend((code_io_handle, text_io_handle))

        child_link_queue = Queue()
        kill = threading.Event()

        parent_link_thread = threading.Thread(target=ThreadExecutioner.execute,
                                              args=(
                                                  self.scrape_parent_links, parent_link_queue, self.site,

                                                  child_link_queue, kill))
        parent_link_thread.setName("StackExchange API Manager")

        child_link_thread = threading.Thread(target=ThreadExecutioner.execute,
                                             args=(self.scrape_child_links, child_link_queue, self.site, code_io_handle,
                                                   text_io_handle, kill))
        child_link_thread.setName("StackOverflow Scraping Manager")

        self.thread_handles.extend((parent_link_thread, child_link_thread))

        for handle in self.thread_handles:
            handle.start()

        kill.wait()

        for handle in self.thread_handles:
            was_alive = ThreadExecutioner.murder(handle)
            print(f'{handle.getName()} is {["not "] if [not was_alive] else [""]} healthy.')

        for file_handle in self.file_handles:
            file_handle.close()

    def scrape_parent_links(self, input_queue: Queue, site: StackOverflow, output_queue: Queue,
                            failure: threading.Event):
        ThreadExecutioner.execute(self.scrape_parent_link, input_queue, site, output_queue, failure)

    def scrape_child_links(self, input_queue: Queue, site: StackOverflow, code_io_handle, text_io_handle,
                           failure: threading.Event):
        ThreadExecutioner.execute(self.scrape_child_link, input_queue, site, code_io_handle,
                                  text_io_handle, failure)

    @staticmethod
    def scrape_parent_link(link: str, used_parents: Queue, site: StackOverflow, output_queue: Queue,
                           failure: threading.Event):
        try:
            has_more = True
            while has_more:
                try:
                    # TODO: handle None response
                    # TODO: make sure actually incrementing page
                    response = site.get_child_links(link, pause=True)
                except SystemExit:
                    raise
                except:
                    # TODO: logging
                    failure.set()
                    raise

                has_more = response[1]
                response = response[0]
                list(map(output_queue.put, response))

                if not has_more:
                    used_parents.put(threading.currentThread())
                    break
        except SystemExit:
            print()
            # TODO: logging

    @staticmethod
    def scrape_child_link(self, link: str, used_children: Queue, site: StackOverflow, code_io_handle, text_io_handle,
                          failure: threading.Event):
        try:
            # TODO: thread this point on in this method for each link
            # TODO: handle None response
            try:
                response = site.process_request(link, pause=True)[0]
            except SystemExit:
                raise
            except:
                # TODO: logging
                failure.set()
                raise

            for code in site.get_code(response):
                snippet = {'snippet': code}

                with self.code_lock:
                    json.dump(snippet, code_io_handle)
                    # code_io_handle.write(code)

            for text in site.get_text(response):
                snippet = {'snippet': text}

                with self.text_lock:
                    json.dump(snippet, text_io_handle)
                    # text_io_handle.write(text)

            used_children.put(threading.current_thread())

        except SystemExit:
            print()
            # TODO: logging
