terraform {
  required_version = ">= 1.0.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
}

provider "aws" {
  region                      = "us-east-1"
  access_key                  = "test"
  secret_key                  = "test"
  s3_use_path_style           = true
  skip_credentials_validation = true
  skip_metadata_api_check     = true
  skip_requesting_account_id  = true
  endpoints {
    sns = "http://localstack:4566"
    sqs = "http://localstack:4566"
  }
}

resource "aws_sns_topic" "order_topic" {
  name = "order_topic"
}

resource "aws_sqs_queue" "order_processor_queue" {
  name = "order_processor_queue"
}

resource "aws_sqs_queue" "order_processed_queue" {
  name = "order_processed_queue"
}

resource "aws_sns_topic_subscription" "sqs_subscription" {
  topic_arn = aws_sns_topic.order_topic.arn
  protocol  = "sqs"
  endpoint  = aws_sqs_queue.order_processor_queue.arn
}

resource "aws_sqs_queue_policy" "sqs_policy" {
  queue_url = aws_sqs_queue.order_processor_queue.id
  policy    = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": "*",
      "Action": "SQS:SendMessage",
      "Resource": "${aws_sqs_queue.order_processor_queue.arn}",
      "Condition": {
        "ArnEquals": {
          "aws:SourceArn": "${aws_sns_topic.order_topic.arn}"
        }
      }
    }
  ]
}
EOF
}

resource "aws_sqs_queue_policy" "order_processed_queue_policy" {
  queue_url = aws_sqs_queue.order_processed_queue.id
  policy    = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": "*",
      "Action": "SQS:SendMessage",
      "Resource": "${aws_sqs_queue.order_processed_queue.arn}"
    }
  ]
}
EOF
}