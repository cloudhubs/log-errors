import json
import pickle

from gensim.models import Doc2Vec

from d2v.training import create_data, train_d2v, create_d2v
from naive_bayes.bayes_trainer import train_bayes

TITLE_FILE: str = "titles"
TRACE_FILE: str = "traces"
MIX: str = "titles_traces"


def train_d2v_service(filenames: list):
    # Clear data files; jank, but simple and effective
    tmpFile = open(TITLE_FILE, "w")
    tmpFile.close()
    tmpFile = open(TRACE_FILE, "w")
    tmpFile.close()

    # Transform data
    for filename in filenames:
        print("Handle file " + filename)
        with open(filename, "r") as input_file:
            create_data(TITLE_FILE, TRACE_FILE, json.loads(input_file.read()))
    print("Parsing complete")
    create_d2v(TITLE_FILE, "./title_dictionary.d2v")
    create_d2v(TRACE_FILE, "./trace_dictionary.d2v")
    return "Training complete"


def convert_to_d2v_service(filenames: list):
    # Format data
    print("Read files")
    titles: list = []
    traces: list = []
    for filename in filenames:
        print("Handle file " + filename)
        with open(filename, "r") as file:
            tmp: list = json.load(file)
            for element in tmp:
                titles.append(element["title"])
                traces.append(element["trace"])
            print(titles[len(titles) - 1])
            print(traces[len(traces) - 1])

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
