from typing import Dict, Union, Any
import threading

import requests

from service.scraper import StackOversight
from service.stack_overflow import StackOverflow
from queue import Queue

keys = ['LuC6vXmC*oswbMCdsWrNuw((']


def init_scrape(language: str):
    # returns a link to a list of SO posts
    posts = scrape_parent_links(language)

    # print(posts)

    link_queue = Queue()
    link_queue.put(posts)

    scraper = StackOversight(keys)

    # start the scraper on that first link
    thread = threading.Thread(target=scraper.start, args=(link_queue,), daemon=False)
    thread.start()

    return thread


def scrape_parent_links(language: str):
    language_tag = ""

    # pick correct language
    # TODO: use various versions of python
    if language == "python":
        language_tag = StackOverflow.Tags.python.value
    elif language == "csharp":
        language_tag = StackOverflow.Tags.csharp.value
    elif language == "java":
        language_tag = StackOverflow.Tags.java.value
    else:
        raise ValueError

    posts = StackOverflow.create_parent_link(sort=StackOverflow.Sorts.votes.value,
                                             order=StackOverflow.Orders.descending.value,
                                             tag=language_tag, page_size=100, key="LuC6vXmC*oswbMCdsWrNuw((")

    return posts


def scrape_link(url: str) -> Dict[str, Union[str, Any]]:
    response = requests.get(url)
    data = {'url': url, 'title': StackOverflow.get_title(response), 'code': StackOverflow.get_code(response),
            'text': StackOverflow.get_text(response),
            'tags': StackOverflow.get_tags(response)}

    return data
