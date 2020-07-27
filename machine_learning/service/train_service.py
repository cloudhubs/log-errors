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
    titles, traces = pre_process_json(filenames)

    # Write data
#    write_line_sentence_format(TITLE_FILE, titles)
#    write_line_sentence_format(TRACE_FILE, traces)
    write_all_line_sentence_format(MIX, titles, traces)
    print("Parsing complete")

    # Train d2vs
    create_d2v(MIX, "./mix_dictionary.d2v")
    return "Training complete"


def convert_to_d2v_service(filenames: list):
    # Format data
    titles, traces = pre_process_json(filenames)

    # Get dictionaries
    print("Get dictionaries")
    d2v = Doc2Vec.load("../api/mix_dictionary.d2v")
    '''
    title_d2v = Doc2Vec.load("../api/title_dictionary.d2v")
    title_d2v.delete_temporary_training_data(keep_doctags_vectors=True, keep_inference=True)
    trace_d2v = Doc2Vec.load("../api/trace_dictionary.d2v")
    trace_d2v.delete_temporary_training_data(keep_doctags_vectors=True, keep_inference=True)
    '''

    # Convert data
    print("Convert data")
    titles_vec = []
    traces_vec = []
    for i in range(len(titles)):
        if i % 1000 == 0:
            print(i)
        '''
        trace = traces[i].split(" ")
        tmp_title = titles[i].split(" ")
        tmp_title.extend(trace)
        result.append(d2v.infer_vector(tmp_title))
        tmp_title = titles[(i + 10) % len(titles)].split(" ")
        tmp_title.extend(trace)
        result_bad.append(d2v.infer_vector(tmp_title))
        '''
        titles_vec.append(d2v.infer_vector(titles[i].split(" "), steps=20))
        traces_vec.append(d2v.infer_vector(traces[i].split(" "), steps=20))

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
                titles.append(" ".join(pre_process_d2v(element["title"])))
                traces.append(" ".join(pre_process_d2v(element["trace"])))
    return titles, traces


bad_chars = re.compile(r"[^a-z0-9]")
porter = PorterStemmer()


def pre_process_d2v(line: str):
    # Scrap bad characters
    tokens = bad_chars.sub(" ", line.lower()).split(" ")
    i: int = 0

    # Remove all empty strings (from multiple spaces)
    tokens = filter(lambda val: len(val) > 0, tokens)

    # Apply porter stemmer and return
    return [porter.stem(word) for word in tokens]


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
