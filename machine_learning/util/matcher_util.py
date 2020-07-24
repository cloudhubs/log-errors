# Data passed in by the user
import random
from _random import Random

from gensim.models.doc2vec import TaggedLineDocument
from gensim.utils import simple_preprocess


class MatchEntry:
    def __init__(self, url, title, trace):
        self.url = url
        self.title = title
        self.trace = trace

    def get_train_version(self):
        return self.title + " " + self.trace


def create_entry_objects(input_data: list):
    result: list = []
    for view in input_data:
        result.append(MatchEntry(view.get("url"), view.get("title"), view.get("trace")))
    return result


def create_dictionary_train_data(input_data: list):
    result: list = []
    for view in input_data:
        result.append(view.get("title"))
        result.append(view.get("trace"))
    random.shuffle(result)
    return result
