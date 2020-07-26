import json
import pickle
import re

from gensim.models import Doc2Vec
from gensim.parsing import PorterStemmer

from d2v.training import create_d2v
from util.matcher_util import create_raw_from_files

TITLE_FILE: str = "titles_d2v"
TRACE_FILE: str = "traces_d2v"
MIX: str = "titles_traces_d2v"


def train_d2v_service(filenames: list):
    # Transform data
    print("Transform data")
#    create_raw_from_files(filenames, TITLE_FILE, TRACE_FILE)
    titles, traces = pre_process_json(filenames)

    # Write data
    write_line_sentence_format(TITLE_FILE, titles)
    write_line_sentence_format(TRACE_FILE, traces)
    print("Parsing complete")

    # Train d2vs
    create_d2v(TITLE_FILE, "./title_dictionary.d2v")
    create_d2v(TRACE_FILE, "./trace_dictionary.d2v")
    return "Training complete"


def convert_to_d2v_service(filenames: list):
    # Format data
    titles, traces = pre_process_json(filenames)

    # Get dictionaries
    print("Get dictionaries")
    title_d2v = Doc2Vec.load("../api/title_dictionary.d2v")
    title_d2v.delete_temporary_training_data(keep_doctags_vectors=True, keep_inference=True)
    trace_d2v = Doc2Vec.load("../api/trace_dictionary.d2v")
    trace_d2v.delete_temporary_training_data(keep_doctags_vectors=True, keep_inference=True)

    # Convert data
    print("Convert data")
    for i in range(len(titles)):
        if i % 1000 == 0:
            print(i)
        titles[i] = title_d2v.infer_vector(titles[i].split(" "))
        traces[i] = trace_d2v.infer_vector(traces[i].split(" "))

    # Write to disk
    print("Saving results")
    with open("./titles_vec.pkl", "wb") as file:
        pickle.dump(titles, file)
    with open("./traces_vec.pkl", "wb") as file:
        pickle.dump(traces, file)

    return "Parsing complete"


def pre_process_json(filenames: list):
    # Format data
    print("Read files")
    titles: list = []
    traces: list = []
    for filename in filenames:
        print("Handle file " + filename)
        with open(filename, "r") as file:
            tmp: list = json.load(file)
            for element in tmp:
                titles.append(pre_process_d2v(element["title"]))
                traces.append(pre_process_d2v(element["trace"]))
    return titles, traces


bad_chars = re.compile(r"[^a-z0-9]")
porter = PorterStemmer()


def pre_process_d2v(line: str):
    # Scrap bad characters
    tokens = bad_chars.sub(" ", line.lower()).split(" ")
    i: int = 0

    # Remove all empty strings (where there were multiple spaces)
    while i < len(tokens):
        if len(tokens[i]) <= 0:
            del tokens[i]
        else:
            i += 1

    # Use porter stemmer
    return " ".join([porter.stem(word) for word in tokens])


def write_line_sentence_format(out_filename: str, elements: list):
    with open(out_filename, "w") as out_file:
        out_file.write(elements[0])
        for i in range(1, len(elements)):
            out_file.write("\n")
            out_file.write(elements[i])