# base build command:   docker build -t redis_store:0.01 -f Dockerfile .
# run command:          docker run -d -p 6379:6379 --name redis_store redis_store:0.01
FROM redis:3.2.8 as redis
# setting save and logLevel arguments:
# save: Redis will save the DB:
#   After 3600 seconds (an hour) if at least 1 change was performed
#   After 300 seconds (5 minutes) if at least 100 changes were performed
#   After 60 seconds if at least 10000 changes were performed
ARG save="3600 1 300 100 60 10000" 
# logLevel: Specify the server verbosity level.
# This can be one of:
#   debug (a lot of information, useful for development/testing)
#   verbose (many rarely useful info, but not a mess like the debug level)
#   notice (moderately verbose, this should be used in a production environment)
#   warning (only very important / critical messages are logged)
ARG logLevel="verbose"

ARG port=6379