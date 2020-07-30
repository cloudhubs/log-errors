import pickle

from gensim.models import Doc2Vec


def match(error: str):
    doc2vec: Doc2Vec = load_d2v()
    nb = load_matcher()


def load_d2v():
    d2v = Doc2Vec.load("dictionary.d2v")
    d2v.delete_temporary_training_data(keep_doctags_vectors=True, keep_inference=True)
    return d2v


def load_matcher():
    with open("error_matcher.pkl", "R") as in_file:
        return pickle.load(in_file)
