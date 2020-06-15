# for Rest API framework
from flask import Flask, jsonify, request, Response
# To rectify cors errors
from flask_cors import cross_origin, CORS
# for scraping
import scraper
from scraper.service.scrape_service import init_scrape

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
@application.route("/scrape/<language>", methods=['POST'])
@cross_origin()
def scrape_language(language: str):
    # only support for 3 languages
    if language not in accepted_languages:
        return Response("<p>Invalid Language, must be of the form csharp, java, python</p>", status=422)

    init_scrape(language)
    # begin the scraping process
    # print(language, request.json)
    return request.json


if __name__ == "__main__":
    application.run(debug=True, port="5000")
