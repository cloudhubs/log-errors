import json

from flask import Flask, request, Response, Blueprint, jsonify
from pymongo import MongoClient
from flask_cors import cross_origin, CORS
from bson.json_util import dumps
from service.scrape_service import scrape_link

db_controller = Blueprint('db_controller', __name__, template_folder='template')
cors = CORS(db_controller)

# app = Flask(__name__)

def getSession():
    con = MongoClient()
    return con


@db_controller.route("/mongo", methods=['GET'])
@cross_origin()
def home():
    return "<p>Hello to the database api</p>"


@db_controller.route("/mongo/test", methods=['POST'])
@cross_origin()
def test_add():
    con = getSession()
    my_coll = con.testdb.coll_name

    # people = [{"_id:": "", "name": "Bilbo Baggins", "age": 50}, {"_id:": "", "name": "Gandalf", "age": 1000},
    #           {"_id:": "", "name": "Thorin", "age": 195}, {"_id:": "", "name": "Balin", "age": 178},
    #           {"_id:": "", "name": "Kili", "age": 77}, {"_id:": "", "name": "Dwalin", "age": 169},
    #           {"_id:": "", "name": "Oin", "age": 167}, {"_id:": "", "name": "Gloin", "age": 158},
    #           {"_id:": "", "name": "Fili", "age": 82}, {"_id:": "", "name": "Bombur", "age": None}]

    recordid = my_coll.insert_many(request.json)

    con.close()
    return "200"


@db_controller.route('/mongo/test', methods=['GET'])
@cross_origin()
def get_all_errors():
    con = getSession()
    data = con.testdb.coll_name.find()

    return dumps(data)


@db_controller.route('/mongo/test', methods=['DELETE'])
@cross_origin()
def delete_all_errors():
    con = getSession()
    myquery = {"name": {"$regex": ".*"}}
    operation = con.testdb.coll_name.delete_many({})

    return {"count": str(operation.deleted_count)}

@db_controller.route("/mongo/test/url", methods=['POST'])
@cross_origin()
def test_add_url():
    # scrape the site.
    site_data = scrape_link(request.json.get('url'))
    # add the site to the db
    con = getSession()
    val = con.testdb.coll_name.insert_one(site_data)
    con.close()

    return {"id": str(val.inserted_id)}
    # reuturn the id
@db_controller.route("/mongo/test/url", methods=['DELETE'])
@cross_origin()
def test_delete_url():
    con = getSession()
    print(request.json.get('url'))
    operation = con.testdb.coll_name.delete_many({"url":request.json.get('url')})
    return {"count": str(operation.deleted_count)}

if __name__ == "__main__":
    db_controller.run(debug=True, port="5000")