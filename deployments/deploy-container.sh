echo 'Stop $PROJECT_NAME Container'
docker stop ${PROJECT_NAME}

echo 'Delete $PROJECT_NAME Container'
docker rm ${PROJECT_NAME}

echo 'Delete $DOCKER_REGISTRY_URL/$PROJECT_NAME Image'
docker images -a | grep ${PROJECT_NAME} | awk '{print $1":"$2}' | xargs docker rmi

echo 'Deploy New Container'
docker run -e "SPRING_PROFILES_ACTIVE=$PROFILE" -p ${PORT}:8080 -d --network ess --name ${PROJECT_NAME} --restart=unless-stopped ${DOCKER_REGISTRY_URL}/${PROJECT_NAME}:${IMAGE_TAG}