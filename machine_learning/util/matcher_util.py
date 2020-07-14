# Data passed in by the user
from gensim.models.doc2vec import TaggedLineDocument


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
