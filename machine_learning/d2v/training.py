from copy import copy, deepcopy

from gensim.models.doc2vec import TaggedLineDocument
from gensim.models import Doc2Vec

from random import shuffle

from util.matcher_util import create_entry_objects


def create_data(input_data: list):
    input_data = create_entry_objects(input_data)
    write_info("good", input_data)

    # Create test data
    tagged = []
    for element in parse_document("good"):
        tagged.append(element)
    return tagged


def write_info(filename: str, input_data: list):
    with open(filename, "w") as file:
        for data in input_data:
            file.write(data.get_train_version())
            file.write('\n')


# Create a tagged document from the given file
def parse_document(filename: str):
    tagged = TaggedLineDocument(filename)
    result = []
    for entry in tagged:
        result.append(entry)
    return result


def train_d2v(tagged_data: list):
    model = Doc2Vec()
    model.build_vocab(deepcopy(tagged_data))
    shuffle(tagged_data)
    model.train(documents=tagged_data, total_examples=model.corpus_count, epochs=model.epochs)
    return model
