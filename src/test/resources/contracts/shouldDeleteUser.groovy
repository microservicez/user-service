package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method "delete"
		url '/api/v1/users/2'
	}
	response {
		status 204
	}
}