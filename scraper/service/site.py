# For the proxy error and the cook_soup type specification
from __future__ import print_function

import threading
import requests
# For nap time if we're nice to the process and the site
from time import sleep
# For site request limit management
import time
# To parse the HTML documents
from bs4 import BeautifulSoup
# For balancing client requests to the site
from scraper.service.site_balancer import SiteBalancer
import inspect


class Site(object):
    balancer = None
    last_pause_time = None

    def __init__(self, sessions: list, timeout_sec: int, limit: int):
        print("Function -> '{}'\t\t".format(inspect.currentframe().f_code.co_name) + " Thread -> " + str(
            threading.get_ident()))

        self.limit = limit
        self.timeout_sec = timeout_sec

        self.back_off = 0

        if not self.balancer:
            self.balancer = SiteBalancer(sessions, timeout_sec, limit)

    def pause(self, pause_time):
        print("Function -> '{}'\t\t".format(inspect.currentframe().f_code.co_name) + " Thread -> " + str(
            threading.get_ident()))
        if not pause_time and self.limit:
            # try to evenly spread the requests out by default
            pause_time = self.timeout_sec / self.limit

        # this is the minimum wait between requests to not be throttled
        min_pause = self.get_min_pause()
        if pause_time < min_pause:
            pause_time = min_pause

        # can not wait less than the back off field if it is set
        if pause_time < self.back_off:
            pause_time = self.back_off

            # returns the field to zero as it should be set only each time it is returned by the api
            self.back_off = 0

        # only wait the diff between the time already elapsed from the last request and the pause_time
        if self.last_pause_time:
            time_elapsed = time.time() - self.last_pause_time

            # if the elapsed time is longer then no need to wait
            if time_elapsed < pause_time:
                pause_time -= time_elapsed
            else:
                pause_time = 0

        # initialize the last_pause_time field and sleep
        sleep(pause_time)
        self.last_pause_time = time.time()

        return self.last_pause_time

    def create_parent_link(self, *args):
        raise NotImplementedError

    def get_child_links(self, *args):
        raise NotImplementedError

    def handle_request(self, url, session):
        raise NotImplementedError

    def get_min_pause(self):
        raise NotImplementedError

    def process_request(self, url: str, pause=False, pause_time=None):
        print("Function -> '{}'\t\t".format(inspect.currentframe().f_code.co_name) + " Thread -> " + str(
            threading.get_ident()))

        # TODO: Set this up to wait on a signal from a timer thread so that it isn't a busy wait
        # get the next id to use or wait until one is ready
        while not self.balancer.is_ready():
            sleep(1)
            print("Waiting...")

        key = next(self.balancer)

        # handle delay if set to spread out requests
        if pause:
            self.pause(pause_time)

        # grab some questions, need to set verify to false otherwise will get an error with the tls certificate
        try:
            response = self.handle_request(url, key)
        except:
            print("Make sure Archituethis is running or comment out setting the proxy environment variables!\n"
                  "Could also be an issue with your token?")
            raise requests.exceptions.ProxyError

        # mark the request as being made
        request_count = self.balancer.capture()

        return response, key, request_count

    @staticmethod
    def cook_soup(response: requests.Response):
        print("Function -> '{}'\t\t".format(inspect.currentframe().f_code.co_name) + " Thread -> " + str(
            threading.get_ident()))

        return BeautifulSoup(response.text, 'html.parser')
