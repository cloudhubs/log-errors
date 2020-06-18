from flask import Flask, request, Response, Blueprint

from flask_cors import cross_origin, CORS

db_controller = Blueprint('db_controller', __name__, template_folder='template')
cors = CORS(db_controller)


@db_controller.route("/mongo", methods=['GET'])
@cross_origin()
def home():
    return "<p>Hello to the database api</p>"


@db_controller.route('/mongo/errors')
@cross_origin()
def get_all_errors():
    raise NotImplementedError
