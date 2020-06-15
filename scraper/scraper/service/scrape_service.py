from scraper.service.scraper import StackOversight
from scraper.service.stack_overflow import StackOverflow
from queue import Queue

keys = ['RGaU7lYPN8L5KbnIfkxmGQ((', '1yfsxJa1AC*GlxN6RSemCQ((']

def init_scrape(language: str):
    posts = scrape_parent_links(language)

    print(posts)

    link_queue = Queue()
    link_queue.put(posts)

    scraper = StackOversight(keys)
    scraper.start(link_queue)


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
                                             tag=language_tag, page_size=100)

    return posts
