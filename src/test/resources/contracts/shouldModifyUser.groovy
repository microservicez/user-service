package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method PUT()
		url '/api/v1/users/1'
		body([
			   "name": "Ada Lace"
		])
		headers {
			accept(applicationJson())
			contentType("application/json")
		}
	}
	response {
		status OK()
		body([
			   "id": 1,
			   "name": "Ada Lace"
		])
		headers {
			contentType("application/json")
		}
	}
}