FROM openjdk:8
RUN mkdir -p /var/app/books-summary-website
COPY ./target/universal/books-summary-website-1.0.zip /var/app/books-summary-website
WORKDIR /var/app/books-summary-website
RUN unzip books-summary-website-1.0.zip
WORKDIR /var/app/books-summary-website/books-summary-website-1.0/bin
CMD ./books-summary-website
