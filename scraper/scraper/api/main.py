# for Rest API framework
from flask import Flask, jsonify, request, Response
# To rectify cors errors
from flask_cors import cross_origin, CORS
# for scraping
import scraper
from scraper.service.scrape_service import init_scrape, scrape_meta

application = Flask(__name__)
cors = CORS(application)

accepted_languages = ["csharp", "java", "python"]


# Default scraper route
@application.route("/")
@cross_origin()
def home():
    return "Hello! Welcome to the scraper api. "


# This method is used to start the scraping process on a given language with given tags.
# These tags are used to get more specific results
# TODO: implement tags in the query string
@application.route("/scrape/<language>", methods=['POST'])
@cross_origin()
def scrape_language(language: str):
    # only support for 3 languages
    if language not in accepted_languages:
        return Response("<p>Invalid Language, must be of the form csharp, java, python</p>", status=422)

    # begin the scraping process
    init_scrape(language)
    return request.json


# TODO: implement tags in the query string
@application.route("/scrape-meta/<language>", methods=['POST'])
@cross_origin()
def scrape_language(language: str):
    # only support for 3 languages
    if language not in accepted_languages:
        return Response("<p>Invalid Language, must be of the form csharp, java, python</p>", status=422)

    # begin the scraping process
    return scrape_meta(language)
    # return request.json


@application.route("/scrape/please_stop", methods=['POST'])
@cross_origin()
def stop_scrape():
    # STOP
    raise NotImplementedError


if __name__ == "__main__":
    application.run(debug=True, port="5000")
