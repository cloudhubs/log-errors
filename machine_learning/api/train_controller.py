# Built based off of the scraper from the StackOversight project
import json

from flask import Flask, jsonify, request, Response, Blueprint
from flask_cors import cross_origin, CORS

from d2v.training import create_data, train_d2v
from naive_bayes.bayes_trainer import train_bayes

train_controller = Blueprint('train_controller', __name__, template_folder='templates')

TRAIN_FILE: str = "train"


@train_controller.route("/train")
@cross_origin()
def home():
    return "Hello! Welcome to the trainer api. "


@train_controller.route("/train/d2v", methods=["POST"])
@cross_origin()
def train_d2v_endpoint():
    print("Request made")
    filenames: list = request.json.get("filenames")

    # Clear data file
    tmpFile = open(TRAIN_FILE, "w")
    tmpFile.close()

    # Transform data
    for filename in filenames:
        print("Handle file " + filename)
        with open(filename, "r") as input_file:
            create_data(TRAIN_FILE, json.loads(input_file.read()))
    print("Parsing complete")
    model = train_d2v(TRAIN_FILE)
    print("Saving model")
    model.save("./dictionary.d2v")
    return "Training complete"


@train_controller.route("/train/naive-bayes", methods=["POST"])
@cross_origin()
def train():
    good_data: str = request.json.get("good_data", type=str)
    train_bayes(good_data)
    return "Training complete"


@train_controller.route("/train/naive-bayes", methods=["GET"])
@cross_origin()
def train():
    good_data: str = request.args.get("good_data", type=str)
    train_bayes(good_data)
    return "Training complete"
