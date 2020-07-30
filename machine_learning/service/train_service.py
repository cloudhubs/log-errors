import json
import pickle
import re

from gensim.models import Doc2Vec
from gensim.parsing import PorterStemmer

from d2v.d2v_train import create_d2v
from util.matcher_util import create_raw_from_files

# File used for temporary storage of Stack Overflow titles while training Doc2Vec
TITLE_FILE: str = "titles_d2v"
# File used for temporary storage of stack traces while training Doc2Vec
TRACE_FILE: str = "traces_d2v"
# File used for storing title/stack trace pairs (title + " " + trace) while training Doc2Vec
MIX: str = "titles_traces_d2v"

# File the Doc2Vec dictionary trained by this program is stored in
OUR_D2V: str = "mix_dictionary.d2v"
# File containing a Doc2Vec dictionary sourced from elsewhere
SOURCED_D2V: str = "doc2vec.bin"

# The initial parameters are pulled from https://github.com/jhlau/doc2vec
# Starting alpha of the Doc2Vec infer_vector
ALPHA = 0.01
# Steps used to compute Doc2Vec's infer_vector
STEPS = 1000

# The regex used to clean stack traces/Stack Overflow titles
bad_chars = re.compile(r"[^a-z0-9]")


def train_d2v_service(filenames: list):
    # Transform data
    print("Transform data")
    titles, traces = pre_process_json(filenames)

    # Write data
    write_all_line_sentence_format(MIX, titles, traces)
    print("Parsing complete")

    # Train d2vs
    create_d2v(MIX, OUR_D2V)
    return "Training complete"


def convert_to_d2v_service(filenames: list, our_dict: bool):
    # Format data
    titles, traces = pre_process_json(filenames)

    # Get dictionaries
    print(our_dict)
    print("Get dictionary")
    name = "../api/"
    if our_dict:
        name += OUR_D2V
    else:
        name += SOURCED_D2V
    d2v = Doc2Vec.load(name)

    # Convert data
    print("Convert data")
    titles_vec = []
    traces_vec = []
    for i in range(len(titles)):
        if i % 1000 == 0:
            print(i)
        titles_vec.append(d2v.infer_vector(titles[i].split(" "), alpha=ALPHA, steps=STEPS))
        traces_vec.append(d2v.infer_vector(traces[i].split(" "), alpha=ALPHA, steps=STEPS))

    # Write to disk
    print("Saving results")
    with open("./titles_vec.pkl", "wb") as file:
        pickle.dump(titles_vec, file)
    with open("./traces_vec.pkl", "wb") as file:
        pickle.dump(traces_vec, file)
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
                titles.append(" ".join(pre_process_line(element["title"])))
                traces.append(" ".join(pre_process_line(element["trace"])))
    return titles, traces


def pre_process_line(line: str):
    # Scrap bad characters
    tokens = bad_chars.sub(" ", line.lower()).strip().split(" ")
    i: int = 0

    # Remove all empty strings (from multiple spaces)
    tokens = filter(lambda val: len(val) > 0, tokens)

    # Apply porter stemmer and return
    return tokens


def write_line_sentence_format(out_filename: str, elements: list):
    with open(out_filename, "w") as out_file:
        out_file.write(elements[0])
        for i in range(1, len(elements)):
            out_file.write("\n")
            out_file.write(elements[i])


def write_all_line_sentence_format(out_filename: str, titles: list, traces: list):
    with open(out_filename, "w") as out_file:
        out_file.write(titles[0])
        out_file.write(" ")
        out_file.write(traces[0])
        for i in range(1, len(titles)):
            out_file.write("\n")
            out_file.write(titles[i])
            out_file.write(" ")
            out_file.write(traces[i])
