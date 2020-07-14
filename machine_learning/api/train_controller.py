# Built based off of the scraper from the StackOversight project
import json

from flask import Flask, jsonify, request, Response, Blueprint
from flask_cors import cross_origin, CORS

from d2v.training import create_data, train_d2v
from naive_bayes.bayes_matcher import train_bayes

train_controller = Blueprint('train_controller', __name__, template_folder='templates')


@train_controller.route("/train")
@cross_origin()
def home():
    return "Hello! Welcome to the trainer api. "


@train_controller.route("/train/d2v", methods=["GET"])
@cross_origin()
def train_d2v_endpoint():
    filename: str = request.args.get("filename", type=str)
    with open(filename, "r") as input_file:
        training_data = create_data(json.loads(input_file.read()))
    model = train_d2v(training_data)
    model.save("./dictionary.d2v")
    return "Training complete"


@train_controller.route("/train/naive-bayes", methods=["GET"])
@cross_origin()
def train():
    good_data: str = request.args.get("good_data", type=str)
    train_bayes(good_data)
    return "Training complete"
