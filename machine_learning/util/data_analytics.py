# Based on https://www.kaggle.com/pmarcelino/comprehensive-data-exploration-with-python/notebook
import json
import math
import pickle

import numpy
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np
from gensim.models import Doc2Vec
from scipy.stats import norm
from sklearn.preprocessing import StandardScaler
from scipy import stats
from naive_bayes.bayes_trainer import create_data, create_bad_data

import warnings

from util.matcher_util import create_entry_objects

warnings.filterwarnings('ignore')

# Data file used
FILE = "C:/Users/Micah/Documents/College/Internship 1 Sophomore Summer-- "\
                          + "Cerny Research Assistant/code/log-errors/train_attempt_2c.json"

print("Open file")
with open(FILE, "r") as input_file:
    train_data = create_bad_data(create_entry_objects(json.loads(input_file.read())))

print("Load d2v")
d2v = Doc2Vec.load("../api/dictionary.d2v")
d2v.delete_temporary_training_data(keep_doctags_vectors=True, keep_inference=True)

print("Vectorize")
length: int = len(train_data)
val: int = length / 4

data = numpy.zeros((length, 100))
print(length)

# Handle traces
print("Traces:")
for i in range(length - 1):
    if (i % val) == 0:
        print("Checkpoint")
    data[i] = d2v.infer_vector(train_data[i].trace.split(" "))
print("Save results")
with open("./traces2.pkl", "wb") as file:
    pickle.dump(data, file)

# Handle titles
print("Titles:")
for i in range(length - 1):
    if (i % val) == 0:
        print("Checkpoint")
    data[i] = d2v.infer_vector(train_data[i].title.split(" "))
print("Save results")
with open("./titles2.pkl", "wb") as file:
    pickle.dump(data, file)

# # Initial look: just a stupid check
# with open(FILE) as file:
#     data = json.load(file)
# lengths: list = [0] * 322
# for dictionary in data:
#     lengths[math.ceil(len(dictionary.get("trace")) / 50)] += 1
# print(lengths)
