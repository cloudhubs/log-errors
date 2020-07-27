# Data passed in by the user
import json
import random
from _random import Random

from gensim.models.doc2vec import TaggedLineDocument
from gensim.utils import simple_preprocess


class MatchEntry:
    def __init__(self, url, title, trace):
        self.url = url
        self.title = title
        self.trace = trace

    def get_train_version(self):
        return self.title + " " + self.trace


def create_raw_from_files(filenames: list, title_file: str, trace_file: str):
    # Clear data files; jank, but simple and effective
    tmpFile = open(title_file, "w")
    tmpFile.close()
    tmpFile = open(trace_file, "w")
    tmpFile.close()

    # Parse and save
    start_newline = False
    for filename in filenames:
        print("Handle file " + filename)
        with open(filename, "r") as input_file:
            create_data(title_file, trace_file, json.loads(input_file.read()), start_newline)


def create_data(titles: str, traces: str, input_data: list, start_newline: bool = False):
    with open(titles, "a") as title_file, open(traces, "a") as trace_file:
        start: int = 0
        if not start_newline:
            title_file.write(input_data[0].get("title"))
            trace_file.write(input_data[0].get("trace"))
            start += 1

        for i in range(start, len(input_data)):
            # Write title
            title_file.write("\n")
            title_file.write(input_data[i].get("title"))

            # Write trace
            trace_file.write("\n")
            trace_file.write(input_data[i].get("trace"))
    input_data.clear()


def create_entry_objects(input_data: list):
    result: list = []
    for view in input_data:
        result.append(MatchEntry(view.get("url"), view.get("title"), view.get("trace")))
    return result


def create_dictionary_train_data(input_data: list):
    result: list = []
    for view in input_data:
        result.append(view.get("title"))
        result.append(view.get("trace"))
    random.shuffle(result)
    return result
