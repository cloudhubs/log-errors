curl --location --request POST 'localhost:8080/errors' \
--header 'Content-Type: application/json' \
--data-raw "{
    \"pathToLogFile\": \"$1\",
    \"pathToLogDirectory\": \"$2\"
}"
