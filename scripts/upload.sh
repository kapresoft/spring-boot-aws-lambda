#!/usr/bin/env bash

# aws lambda list-functions --region us-east-1
project_version=0.0.1
archive=spring-boot-lambda-${project_version}.jar
archive_md5=$(md5 -q target/${archive})
ts="$(date +%Y-%m-%d)-${archive_md5}"
archiveS3=spring-boot-lambda-${project_version}-${ts}.jar
s3Bucket=kapresoft
s3Key=lambda/${archiveS3}
s3Uri=s3://${s3Bucket}/${s3Key}

upload_only_opt=$1
s3_enabled=no
update_only=no

if [[ "${1}" == "s3" ]]; then
    s3_enabled=yes
elif [[ "${1}" == "update" ]]; then
    update_only=yes
elif [[ "${1}" == "update-s3" ]]; then
    s3_enabled=yes
    update_only=yes
fi

function updateLambdaFunctionFromS3() {
    echo "Uploading Lambda to S3 Bucket ${s3Uri} ..." && \
    local lscmd="aws s3 ls ${s3Bucket}"
    local cmd="aws s3 cp target/${archive} ${s3Uri}"
    echo Executing: ${cmd}
    eval ${cmd}

    echo "Updating lambda function..."
    local cmd="aws lambda update-function-code \
            --region us-east-1 \
            --function-name spring-boot-demo \
            --s3-bucket ${s3Bucket} --s3-key ${s3Key}"
    echo Executing: ${cmd}
    eval ${cmd}
}

function updateLambdaFunction() {
    echo "Updating lambda function..."
    local cmd="aws lambda update-function-code \
            --region us-east-1 \
            --function-name spring-boot-demo \
            --zip-file fileb://target/${archive}"
    echo Executing: ${cmd}
    eval ${cmd}
}

if [[ "${update_only}" == "yes" ]]; then
    if [[ "${s3_enabled}" == "yes" ]]; then
        updateLambdaFunctionFromS3
    else
        updateLambdaFunction
    fi
    exit
fi

if [[ "${s3_enabled}" == "yes" ]]; then
    mvn package -P!sbmp,shade -DskipTests && updateLambdaFunctionFromS3
else
    mvn package -P!sbmp,shade -DskipTests && updateLambdaFunction
fi




