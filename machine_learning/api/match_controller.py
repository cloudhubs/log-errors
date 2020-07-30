# Built based off of the scraper from the StackOversight project
import json

from flask import Flask, jsonify, request, Response, Blueprint
from flask_cors import cross_origin, CORS

match_controller = Blueprint('match_controller', __name__, template_folder='templates')


@match_controller.route("/matcher")
@cross_origin()
def home():
    return "Hello! Welcome to the matcher api. "


@match_controller.route("/matcher/machine-learning", methods=["GET"])
@cross_origin()
def match_ml_endpoint():
    error: str = request.args.get("error", type=str)
    titles: list = request.args.get("titles", type=list)
