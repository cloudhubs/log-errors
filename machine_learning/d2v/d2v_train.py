import json

from gensim.models import Doc2Vec
from gensim.models.doc2vec import TaggedDocument, TaggedLineDocument


def create_d2v(filename: str, result_name: str):
    print("Make " + result_name)
    model = train_d2v(filename)
    print("Saving " + result_name)
    model.save(result_name)


def train_d2v(data_file: str):
    tagged = TaggedLineDocument(data_file)
    model = Doc2Vec(tagged, vector_size=150, window=8, min_count=1, workers=4, epochs=10)
    model.train(tagged, total_examples=model.corpus_count, epochs=model.epochs)
    return model
