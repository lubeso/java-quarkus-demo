services = [
	{"name": "foo"},
	{"name": "bar"},
]

for i, config in enumerate(services):
	local_resource(
		labels=["services"],
		name=config.get("name"),
		serve_dir=config.get("name"),
		serve_cmd=[
			"quarkus",
			"dev",
			"--debug-port={}".format(5005 + i),
		],
		serve_env=config.get("serve_env", {}) | {
			"QUARKUS_HTTP_PORT": "{}".format(8080 + i),
			"QUARKUS_HTTP_TEST_PORT": "{}".format(9090 + i),
		},
	)
