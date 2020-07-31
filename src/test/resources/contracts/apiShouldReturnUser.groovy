package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method GET()
		url "/api/v1/users/1"
		headers {
			accept(applicationJson())
		}
	}
	response {
		status 200
		body([
			"id": "1",
			"name": "James Holden"
		])
		headers {
			contentType(applicationJson())
		}
	}
}