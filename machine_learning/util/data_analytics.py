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

# Create list
input = ["C:/Users/Micah/Documents/College/Internship 1 Sophomore Summer-- Cerny Research Assistant/code/log-errors/train_attempt_3_0.json",
"C:/Users/Micah/Documents/College/Internship 1 Sophomore Summer-- Cerny Research Assistant/code/log-errors/train_attempt_3_1.json",
"C:/Users/Micah/Documents/College/Internship 1 Sophomore Summer-- Cerny Research Assistant/code/log-errors/train_attempt_3_2.json",
"C:/Users/Micah/Documents/College/Internship 1 Sophomore Summer-- Cerny Research Assistant/code/log-errors/train_attempt_3_3.json"];
for i in range(0, len(input)):
    input[i] = open(input[i], "r")
