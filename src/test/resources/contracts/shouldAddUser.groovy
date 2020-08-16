package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method POST()
		url '/api/v1/users/'
		body([
			   "name": "James Goshling"
		])
		headers {
			accept(applicationJson())
			contentType("application/json")
		}
	}
	response {
		status 201
		body([
			   "name": fromRequest().body("name"),
		])
		headers {
			contentType("application/json")
		}
	}
}