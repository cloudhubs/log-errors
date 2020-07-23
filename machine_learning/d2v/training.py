from copy import copy, deepcopy

from gensim.models.doc2vec import TaggedLineDocument
from gensim.models import Doc2Vec

from random import shuffle

from util.FileCorpus import FileCorpus
from util.matcher_util import create_entry_objects


def create_data(title_file: str, trace_file: str, input_data: list):
    entries: list = create_entry_objects(input_data)
    with open(title_file, "a") as titles, open(trace_file, "a") as traces:
        for entry in entries:
            titles.write(entry.title)
            traces.write(entry.trace)
    entries.clear()
    input_data.clear()


def train_d2v(data_file: str):
    tagged = TaggedLineDocument(data_file)
    model = Doc2Vec()
    print("Build vocab")
    model.build_vocab(documents=tagged)
    print("Training model")
    model.train(documents=tagged, total_examples=model.corpus_count, epochs=10)
    return model
