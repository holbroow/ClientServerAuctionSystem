#!/bin/bash

gnome-terminal -- bash -c "cd \"$(pwd)/$server\" && rmiregistry; exec bash"
gnome-terminal -- bash -c "cd \"$(pwd)/$server\" && java Server; exec bash"
gnome-terminal -- bash -c "cd \"$(pwd)/$client\"; exec bash"