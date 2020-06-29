# for Rest API framework
from flask import Flask, jsonify, request, Response, Blueprint
# To rectify cors errors
from flask_cors import cross_origin, CORS
# for scraping
from service.scrape_service import init_scrape, scrape_parent_links

scrape_controller = Blueprint('scrape_controller', __name__, template_folder='templates')
cors = CORS(scrape_controller)
# app = Flask(__name__)


accepted_languages = ["csharp", "java", "python"]


# Default scraper route
@scrape_controller.route("/scrape")
@cross_origin()
def home():
    return "Hello! Welcome to the scraper api. "


# This method is used to start the scraping process on a given language with given tags.
# These tags are used to get more specific results
# TODO: implement tags in the query string
@scrape_controller.route("/scrape/<language>", methods=['POST'])
@cross_origin()
def scrape_language(language: str):
    # only support for 3 languages
    if language not in accepted_languages:
        return Response("<p>Invalid Language, must be of the form csharp, java, python</p>", status=422)

    # begin the scraping process
    working_thread = init_scrape(language)
    return request.json


# TODO: implement tags in the query string
@scrape_controller.route("/scrape-meta/<language>", methods=['GET'])
@cross_origin()
def scrape_parent_language(language: str):
    # only support for 3 languages
    if language not in accepted_languages:
        return Response("<p>Invalid Language, must be of the form csharp, java, python</p>", status=422)

    # begin the scraping process
    return scrape_parent_links(language)
    # return request.json


@scrape_controller.route("/scrape/stop", methods=['POST'])
@cross_origin()
def stop_scrape():
    # STOP
    raise NotImplementedError

if __name__ == "__main__":
    scrape_controller.run(debug=True, port="5000")