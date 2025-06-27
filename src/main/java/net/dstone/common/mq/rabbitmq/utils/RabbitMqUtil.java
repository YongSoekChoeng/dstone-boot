package net.dstone.common.mq.rabbitmq.utils;

public class RabbitMqUtil {
	
	public static void main(String[] args) {
		
		System.out.println( makeDefualtConfig() );
	}
	
	/**
	 * RabbitMQ 기본설정파일 생성 메소드.
	 * @return
	 */
	public static String makeDefualtConfig() {
		StringBuffer buff = new StringBuffer();
		buff.append("{").append("\n");
		buff.append("    \"rabbit_version\": \"3.13.7\",").append("\n");
		buff.append("    \"rabbitmq_version\": \"3.13.7\",").append("\n");
		buff.append("    \"product_name\": \"RabbitMQ\",").append("\n");
		buff.append("    \"product_version\": \"3.13.7\",").append("\n");
		buff.append("    \"users\": [").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"name\": \"guest\",").append("\n");
		buff.append("            \"password_hash\": \"dbniyabyH5mxPHGJyJpK2e7R4XyANERH/Lss27YMru0X4n93\",").append("\n");
		buff.append("            \"hashing_algorithm\": \"rabbit_password_hashing_sha256\",").append("\n");
		buff.append("            \"tags\": [").append("\n");
		buff.append("                \"administrator\"").append("\n");
		buff.append("            ],").append("\n");
		buff.append("            \"limits\": {}").append("\n");
		buff.append("        },").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"name\": \"jysn007\",").append("\n");
		buff.append("            \"password_hash\": \"s87PV0Z/0MiRA2Ve6MzWbLzG00In5EFLxjIa1kocaBSqHr55\",").append("\n");
		buff.append("            \"hashing_algorithm\": \"rabbit_password_hashing_sha256\",").append("\n");
		buff.append("            \"tags\": [],").append("\n");
		buff.append("            \"limits\": {}").append("\n");
		buff.append("        }").append("\n");
		buff.append("    ],").append("\n");
		buff.append("    \"vhosts\": [").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"name\": \"dstone-mq\",").append("\n");
		buff.append("            \"description\": \"\",").append("\n");
		buff.append("            \"tags\": [],").append("\n");
		buff.append("            \"default_queue_type\": \"classic\",").append("\n");
		buff.append("            \"metadata\": {").append("\n");
		buff.append("                \"description\": \"\",").append("\n");
		buff.append("                \"tags\": [],").append("\n");
		buff.append("                \"default_queue_type\": \"classic\"").append("\n");
		buff.append("            }").append("\n");
		buff.append("        }").append("\n");
		buff.append("    ],").append("\n");
		buff.append("    \"permissions\": [").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"user\": \"jysn007\",").append("\n");
		buff.append("            \"vhost\": \"dstone-mq\",").append("\n");
		buff.append("            \"configure\": \".*\",").append("\n");
		buff.append("            \"write\": \".*\",").append("\n");
		buff.append("            \"read\": \".*\"").append("\n");
		buff.append("        },").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"user\": \"guest\",").append("\n");
		buff.append("            \"vhost\": \"dstone-mq\",").append("\n");
		buff.append("            \"configure\": \".*\",").append("\n");
		buff.append("            \"write\": \".*\",").append("\n");
		buff.append("            \"read\": \".*\"").append("\n");
		buff.append("        }").append("\n");
		buff.append("    ],").append("\n");
		buff.append("    \"topic_permissions\": [").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"user\": \"jysn007\",").append("\n");
		buff.append("            \"vhost\": \"dstone-mq\",").append("\n");
		buff.append("            \"exchange\": \"\",").append("\n");
		buff.append("            \"write\": \".*\",").append("\n");
		buff.append("            \"read\": \".*\"").append("\n");
		buff.append("        },").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"user\": \"guest\",").append("\n");
		buff.append("            \"vhost\": \"dstone-mq\",").append("\n");
		buff.append("            \"exchange\": \"\",").append("\n");
		buff.append("            \"write\": \".*\",").append("\n");
		buff.append("            \"read\": \".*\"").append("\n");
		buff.append("        }").append("\n");
		buff.append("    ],").append("\n");
		buff.append("    \"parameters\": [],").append("\n");
		buff.append("    \"global_parameters\": [").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"name\": \"internal_cluster_id\",").append("\n");
		buff.append("            \"value\": \"rabbitmq-cluster-id-g11AOL_aV5VzoW_8EA4uww\"").append("\n");
		buff.append("        }").append("\n");
		buff.append("    ],").append("\n");
		buff.append("    \"policies\": [],").append("\n");
		buff.append("    \"queues\": [").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"name\": \"notifications\",").append("\n");
		buff.append("            \"vhost\": \"dstone-mq\",").append("\n");
		buff.append("            \"durable\": true,").append("\n");
		buff.append("            \"auto_delete\": false,").append("\n");
		buff.append("            \"arguments\": {").append("\n");
		buff.append("                \"x-queue-type\": \"classic\"").append("\n");
		buff.append("            }").append("\n");
		buff.append("        }").append("\n");
		buff.append("    ],").append("\n");
		buff.append("    \"exchanges\": [],").append("\n");
		buff.append("    \"bindings\": [").append("\n");
		buff.append("        {").append("\n");
		buff.append("            \"source\": \"amq.direct\",").append("\n");
		buff.append("            \"vhost\": \"dstone-mq\",").append("\n");
		buff.append("            \"destination\": \"notifications\",").append("\n");
		buff.append("            \"destination_type\": \"queue\",").append("\n");
		buff.append("            \"routing_key\": \"notifications\",").append("\n");
		buff.append("            \"arguments\": {}").append("\n");
		buff.append("        }").append("\n");
		buff.append("    ]").append("\n");
		buff.append("}").append("\n");
		return buff.toString();
	}
}
