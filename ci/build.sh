#!/bin/sh -ex

cd source

mvn -e install
mkdir ../put
cp target/quote-service-1.0.0.jar ../put
