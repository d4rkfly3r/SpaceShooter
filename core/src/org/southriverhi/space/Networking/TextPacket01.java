/*
 * This file is part of SpaceShooter.
 *
 *  SpaceShooter is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  SpaceShooter is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with SpaceShooter.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.southriverhi.space.Networking;

/**
 * @author Joshua Freedman
 * @see org.southriverhi.space.Networking.Packet
 */
public class TextPacket01 extends Packet {

    /**
     * Saved Text.
     */
    private String uInput;

    /**
     * Paramter is the text to be serialized and sent to server.
     * @param userInput
     */
    public TextPacket01(String userInput) {
        uInput = userInput;
    }

    @Override
    public short getPacketId() {
        return 1;
    }

    /**
     * Returns the saved text.
     * @return
     */
    public String getUserInput(){
        return uInput;
    }
}
