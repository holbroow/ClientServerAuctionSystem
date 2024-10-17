#!/bin/bash

gnome-terminal -- bash -c "cd \"$(pwd)/server\" && rmiregistry; exec bash"
sleep 2
gnome-terminal -- bash -c "cd \"$(pwd)/server\" && java Server; exec bash"