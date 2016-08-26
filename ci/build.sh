#!/bin/sh -ex

cd source

mvn -e install
cp target/quote-service-1.0.0.jar /tmp
