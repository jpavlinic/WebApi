# Build
mvn clean package && docker build -t croatia.rit.edu/pavlinic-josip-p2 .

# RUN

docker rm -f pavlinic-josip-p2 || true && docker run -d -p 8080:8080 -p 4848:4848 --name pavlinic-josip-p2 croatia.rit.edu/pavlinic-josip-p2 