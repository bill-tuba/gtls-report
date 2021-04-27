# gtls-report

Report sample homework

## Installation

Download from https://github.com/badams/gtls-report

## Usage

The project may be executed as a cli application or a 
standalone api-server

Run the project as a cli application via `clojure`:

    $ ./bin/run-cli <OPTIONS>

Run the project as a api server via `clojure**:

    $ ./bin/run-server <OPTIONS>

## Options
### CLI 
    usage: badams.cli
           -f  path to 'csv' input file
               can be comma, space or pipe delimited.
### SERVER
    usage: badams.api
          -p  open port to start api (defaults to 8081**

## Examples:
### CLI
    
    $ ./bin/run-cli -f ./resources/sample.csv
    
    $ ./bin/run-cli -f ./resources/sample.space-sv
    
    $ ./bin/run-cli -f ./resources/sample.pipe-sv
    
### SERVER    
    $ ./bin/run-server -p 3000

### Running tests

    $ ./bin/kaocha

## ... tests+coverage

    $ ./bin/coverage
    
## Uberjar
### CLI
    
    $ ./bin/build-cli
    $ java -jar report-cli.jar -f ./resources/sample.csv
    
### SERVER

    $ ./bin/build-server

    $ java -jar report-server.jar -p 3000


## Examples: (server)
    POST-ing new records
    
    $ \
    for i in 1 2 3 4 5; do
    curl -X POST -H "Content-Type: text/plain" -d "$foo${i},bar,email${i},blue,1/1/200${i}" http://localhost:3000/records/ 
    done

    for i in 1 2 3 4 5; do 
    curl -X POST -H "Content-Type: text/plain" 
    -d "$foo${i},bar,email${i},blue,1/1/200${i}"
    http://localhost:3000/records
    
   GET-ing records: (assuming jq is installed)
    
    $ curl http://localhost:3000/records/email     | jq
    ...
    
    $ curl http://localhost:3000/records/name      | jq
    ...
    
    $ curl http://localhost:3000/records/birthdate | jq
    ... 
    
    $ curl http://localhost:3000/OHNOES!
     ;; NOT FOUND
