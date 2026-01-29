cmake_minimum_required(VERSION 3.22.1)

# Naming the project (like putting a name on your lego creation)
project("stellar-physics")

# Creating the library (building the engine block)
add_library(
        stellar-physics # The name of our library
        SHARED          # It's a shared library (like a tool anyone can borrow)
        native-lib.cpp) # The C++ source file (the instructions)

# Finding the log library (looking for the pen to write logs)
find_library(
        log-lib # We'll call it log-lib variable
        log)    # The system name is "log"

# Linking libraries (connecting the pen to our engine so it can write)
target_link_libraries(
        stellar-physics
        ${log-lib})
