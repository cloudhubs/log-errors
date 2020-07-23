import json
import pickle

from gensim.models import Doc2Vec

from d2v.training import create_data, train_d2v
from naive_bayes.bayes_trainer import train_bayes

TITLE_FILE: str = "titles"
TRACE_FILE: str = "traces"


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

    # Train on titles
    print("Make titles")
    model_title = train_d2v(TITLE_FILE)
    print("Saving titles")
    model_title.save("./title_dictionary.d2v")
    model_title = None

    # Train on traces
    print("Make traces")
    model_trace = train_d2v(TRACE_FILE)
    print("Saving traces")
    model_trace.save("./trace_dictionary.d2v")
    model_trace = None

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
    d2v_title = Doc2Vec.load("../api/title_dictionary.d2v")
    d2v_title.delete_temporary_training_data(keep_doctags_vectors=True, keep_inference=True)
    d2v_trace = Doc2Vec.load("../api/title_dictionary.d2v")
    d2v_trace.delete_temporary_training_data(keep_doctags_vectors=True, keep_inference=True)

    # Convert data
    print("Convert data")
    for i in range(len(titles)):
        if i % 1000 == 0:
            print(i)
        titles[i] = d2v_title.infer_vector(titles[i].split(" "))
        traces[i] = d2v_trace.infer_vector(traces[i].split(" "))

    # Write to disk
    print("Saving results")
    with open("./titles_vec.pkl", "wb") as file:
        pickle.dump(titles, file)
    with open("./traces_vec.pkl", "wb") as file:
        pickle.dump(traces, file)

    return "Parsing complete"
