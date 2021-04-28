# gtls-report

Report sample homework

## Usage

The project may be executed as a cli application or a 
standalone api-server

Run the project as a cli application via `clojure`:

    $ ./bin/run-cli <FILES>

Run the project as a api server via `clojure**:

    $ ./bin/run-server <PORT>

## Examples:
### CLI
    
    $ ./bin/run-cli ./resources/sample.csv ./resources/sample.space-sv ./resources/sample.pipe-sv
    
### SERVER    
    $ ./bin/run-server 3000

### Running tests

    $ ./bin/kaocha

### ... tests+coverage

    $ ./bin/coverage
    
## Uberjar
### CLI
    
    $ ./bin/build-cli
    $ java -jar report-cli.jar ./resources/sample.csv  ...other files etc
    
### SERVER

    $ ./bin/build-server

    $ java -jar report-server.jar 3000

## Examples: (server)
    POST record endpoint:
    
    $ \
    for i in 1 2 3 4 5; do
    curl -X POST -H "Content-Type: text/plain" -d "$foo${i},bar,email${i},blue,1/1/200${i}" http://localhost:3000/records/ 
    done

    for i in 1 2 3 4 5; do 
    curl -X POST -H "Content-Type: text/plain" 
    -d "$foo${i},bar,email${i},blue,1/1/200${i}"
    http://localhost:3000/records
    
   GET records endpoints: (assumes `jq` installed)
    
    $ curl http://localhost:3000/records/email     | jq
    ...
    
    $ curl http://localhost:3000/records/name      | jq
    ...
    
    $ curl http://localhost:3000/records/birthdate | jq
    ... 
    
    $ curl http://localhost:3000/OHNOES!
     ;; NOT FOUND

## Assumptions 
To keep things simple(r) these programs accept args not "options".
input checking was excluded:
`Email` (reg-ex to check Email) or `FavoriteColor`
(enumeration like ROYGBIV) - The idea is to do some basic checks (like `DateOfBirth`)
bailing out on that line of input if appropriate.

It was assumed all of the fields are mandatory.


POST can return Location Header data but since individually POSTed data is not retrievable
directly I send back the Location as `/records/unsorted` to get all of the items.
