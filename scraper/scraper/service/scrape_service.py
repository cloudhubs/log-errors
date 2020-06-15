from scraper.service.scraper import StackOversight
from scraper.service.stack_overflow import StackOverflow
from queue import Queue


def init_scrape(language: str):
    keys = ['RGaU7lYPN8L5KbnIfkxmGQ((', '1yfsxJa1AC*GlxN6RSemCQ((']
    language_tag = ""

    # pick correct language
    if language == "python":
        language_tag = StackOverflow.Tags.python.value
    elif language == "csharp":
        language_tag = StackOverflow.Tags.csharp.value
    elif language == "java":
        language_tag = StackOverflow.Tags.java.value

    posts = StackOverflow.create_parent_link(sort=StackOverflow.Sorts.votes.value,
                                             order=StackOverflow.Orders.descending.value,
                                             tag=language_tag, page_size=100)

    print(posts)

    link_queue = Queue()
    link_queue.put(posts)

    scraper = StackOversight(keys)
    scraper.start(link_queue)
