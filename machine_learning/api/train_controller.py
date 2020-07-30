# Built based off of the scraper from the StackOversight project
import json

from flask import Flask, jsonify, request, Response, Blueprint
from flask_cors import cross_origin, CORS

from naive_bayes.bayes_trainer import train_bayes
from service.train_service import train_d2v_service, convert_to_d2v_service
from tfidf.tfidf_train import train_tfidf_service, convert_to_tfidf_service

train_controller = Blueprint('train_controller', __name__, template_folder='templates')

TITLE_FILE: str = "titles"
TRACE_FILE: str = "traces"


@train_controller.route("/train")
@cross_origin()
def home():
    return "Hello! Welcome to the trainer api. "


@train_controller.route("/train/d2v", methods=["POST"])
@cross_origin()
def train_d2v_endpoint():
    return train_d2v_service(request.json.get("filenames"))


@train_controller.route("/train/d2v/convert", methods=["POST"])
@cross_origin()
def convert_to_d2v_endpoint():
    return convert_to_d2v_service(request.json.get("filenames"), request.json.get("our_dict"))


@train_controller.route("/train/naive-bayes", methods=["POST"])
@cross_origin()
def train_bayes_endpoint():
    good_data: str = request.json.get("good_data", type=str)
    train_bayes(good_data)
    return "Training complete"
