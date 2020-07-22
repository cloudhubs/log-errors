from copy import copy, deepcopy

from gensim.models.doc2vec import TaggedLineDocument
from gensim.models import Doc2Vec

from random import shuffle

from util.FileCorpus import FileCorpus
from util.matcher_util import create_entry_objects


def create_data(train_file_name: str, input_data: list):
    write_info(train_file_name, create_entry_objects(input_data))


def write_info(filename: str, input_data: list):
    count: int = 0
    with open(filename, "a") as file:
        for data in input_data:
            # Create data
            file.write(data.get_train_version())
            file.write('\n')

            # Debug
            count += 1
            if count % 1000000 == 0:
                print(count)


def train_d2v(data_file: str):
    tagged = TaggedLineDocument(data_file)
    model = Doc2Vec()
    print("Build vocab")
    model.build_vocab(documents=tagged)
    print("Training model")
    model.train(documents=tagged, total_examples=model.corpus_count, epochs=10)
    return model
