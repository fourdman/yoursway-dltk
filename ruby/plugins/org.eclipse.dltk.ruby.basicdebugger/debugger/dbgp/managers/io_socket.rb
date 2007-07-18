###############################################################################
# Copyright (c) 2005, 2007 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#

###############################################################################

require 'socket'
require 'io/wait'
require 'thread'

require 'logger'

module XoredDebugger
    class SocketIOManager
		include Logger

        def initialize(host, port, printer)
			@mutex = Mutex.new
            @socket = TCPSocket.new(host, port)
            @printer = printer
        end

        def send(command, data)
			@mutex.synchronize do
	            xml = @printer.print(command, data)
	            
	            #DEBUGGER: [NUMBER] [NULL] XML(data) [NULL]
	            @socket.write(xml.length.to_s)
	            @socket.putc(0)
	            @socket.write(xml)
	            @socket.putc(0)
	            @socket.flush

	            log('>>> ' + xml)
			end
        end

        def has_data?
            @socket.ready?
        end

        def receive()
			@mutex.synchronize do
	            #IDE: command [SPACE] [args] -- data [NULL]
	            line = ''
	            while((ch = @socket.getc) != 0)
	                line << ch
	            end
				
				log('<<< ' + line)
	            
	            line
			end
        end

        def close
            @socket.close
        end
    end # class SocketIOManager
end # module XoredDebugger