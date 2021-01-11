#!/bin/bash

find /etc -type d | sed 's#/#\\#g' | sed 's#\\#C:\\#'
