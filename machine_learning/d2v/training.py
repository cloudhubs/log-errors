from copy import copy, deepcopy

from gensim import utils
from gensim.models.doc2vec import TaggedLineDocument
from gensim.models import Doc2Vec

import numpy as np

from random import shuffle

from sklearn.naive_bayes import MultinomialNB


# Data passed in by the user
class MatchEntry:
    def __init__(self, url, title, trace):
        self.url = url
        self.title = title
        self.trace = trace


# Create the full training corpus, given the 'good' training set
def create_data(input_data: list):
    input_data = create_entry_objects(input_data)
    write_info("good", input_data)

    # Create test data
    tagged = []
    for element in parse_document("good"):
        tagged.append(element)
    return tagged


def create_entry_objects(input_data: list):
    result: list = []
    for view in input_data:
        result.append(MatchEntry(view.get("url"), view.get("title"), view.get("trace")))
    return result


# Create a tagged document from the given file
def parse_document(filename: str):
    tagged = TaggedLineDocument(filename)
    result = []
    for entry in tagged:
        result.append(entry)
    return result


def write_info(filename: str, input_data: list):
    with open(filename, "w") as file:
        for data in input_data:
            file.write(data.title)
            file.write("\n")
            file.write(data.trace)
            file.write('\n')


def train_d2v(tagged_data: list):
    model = Doc2Vec()
    model.build_vocab(deepcopy(tagged_data))
    shuffle(tagged_data)
    model.train(documents=tagged_data, total_examples=model.corpus_count, epochs=model.epochs)
    return model
