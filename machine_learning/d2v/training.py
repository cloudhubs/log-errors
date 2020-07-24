from copy import copy, deepcopy

from gensim.models.doc2vec import TaggedLineDocument
from gensim.models import Doc2Vec

from util.matcher_util import create_entry_objects, create_dictionary_train_data


def create_data(titles: str, traces: str, input_data: list):
    with open(titles, "a") as title_file, open(traces, "a") as trace_file:
        title_file.write(input_data[0].get("title"))
        trace_file.write(input_data[0].get("trace"))
        for i in range(1, len(input_data)):
            # Write title
            title_file.write("\n")
            title_file.write(input_data[i].get("title"))

            # Write trace
            trace_file.write("\n")
            trace_file.write(input_data[i].get("trace"))
    input_data.clear()


def create_d2v(filename: str, result_name: str):
    print("Make " + result_name)
    model = train_d2v(filename)
    print("Saving " + result_name)
    model.save(result_name)


def train_d2v(data_file: str):
    tagged = TaggedLineDocument(data_file)
    model = Doc2Vec()
    print("Build vocab")
    model.build_vocab(documents=tagged)
    print("Training model")
    model.train(documents=tagged, total_examples=model.corpus_count, epochs=model.iter)
    return model
