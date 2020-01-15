FROM ubuntu:latest
RUN apt-get update -y
FROM python:3
COPY . /app
WORKDIR /app
RUN pip install --no-cache-dir -r requirements.txt
ENTRYPOINT ["python"]
CMD ["app.py"]
EXPOSE 5002

