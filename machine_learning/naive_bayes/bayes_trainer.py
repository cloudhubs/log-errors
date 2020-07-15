import json
from copy import copy

import numpy
import pickle

from gensim.models import Doc2Vec
from sklearn.metrics import accuracy_score, classification_report
from sklearn.model_selection import train_test_split
from sklearn.naive_bayes import MultinomialNB
from sklearn.preprocessing import minmax_scale

from util.matcher_util import create_entry_objects


# Trains the Naive Bayes
def train_bayes(good_data_filename: str):
    with open(good_data_filename, "r") as input_file:
        good_data = create_entry_objects(json.loads(input_file.read()))

    # Load Doc2Vec model
    d2v = Doc2Vec.load("dictionary.d2v")
    d2v.delete_temporary_training_data(keep_doctags_vectors=True, keep_inference=True)

    # Create training data
    categories = [ "match", "no_match" ]
    bad_data = create_bad_data(good_data)

    # label_names: The categories ['match', 'no_match']
    # labels: ID which category the element is in
    # feature_names: ['title', 'stack_trace']
    # feature: [ titleD2v, traceD2v ]

    # Prepare training variables
    length: int = len(good_data)
    data = numpy.zeros((length * 2, 100))
    cats = numpy.zeros(length * 2)

    # Generate data
    for i in range(length - 1):
        data[i] = d2v.infer_vector(good_data[i].get_train_version().split(" "))
        cats[i] = 0
        data[i + length] = d2v.infer_vector(bad_data[i].get_train_version().split(" "))
        cats[i + length] = 1

    # Fit data
    data = minmax_scale(data)

    # Partition data
    train_data, test_data, train_cats, test_cats = train_test_split(
        data, cats, test_size=0.40, random_state=42
    )

    # Train
    nb = MultinomialNB()
    nb.fit(train_data, train_cats)

    # Analyze
    prediction = nb.predict(test_data)
    report(test_cats, prediction)

    # Persist classifier to disk
    with open("error_matcher.pkl", "W") as out_file:
        pickle.dump(nb, out_file)


def create_bad_data(input_data: list):
    # Generate 'bad' data
    bad_data: list = []
    len_input: int = len(input_data)
    for index in range(0, len_input - 1):
        tmp = copy(input_data[index])
        tmp.title = input_data[(index + 1) % len_input].title
        bad_data.append(tmp)
    return bad_data


# Code from https://towardsdatascience.com/understand-how-to-transfer-your-paragraph-to-vector-by-doc2vec-1e225ccf102
def report(data, prediction):
    print('Accuracy:%.2f%%' % (accuracy_score(data, prediction) * 100))
    print('Classification Report:')
    print(classification_report(data, prediction))
