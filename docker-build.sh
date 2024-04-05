#docker network create movesongnet

cd config
docker build -t movesong/config .
#docker run --network movesongnet -d -p 8081:8081 movesong/config
cd ..

cd discovery
docker build -t movesong/discovery .
#docker run --network movesongnet -d -p 8085:8085 movesong/discovery
cd ..

cd gateway
docker build -t movesong/gateway .
#docker run --network movesongnet -d -p 8080:8080 movesong/gateway
cd ..

cd user
docker build -t movesong/user .
#docker run --network movesongnet -d -p 8082:8082 movesong/user
cd ..
cd ..
#cd email
#docker build -t movesong/email .
#cd ..

cd movesong-frontend
docker build -t movesong/frontend .
cd ..
