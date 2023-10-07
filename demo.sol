// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract RockPaperScissors {
    //0x358AA13c52544ECCEF6B0ADD0f801012ADAD5eE3
    address public owner;
    uint256 public stake = 0.0001 ether;
    uint256 public pot = 0;

    enum Move { None, Rock, Paper, Scissors }

    struct Game {
        address player;
        Move playerMove;
        Move houseMove;
        bool resolved;
        bool rewarded;
    }

    Game[] public games;

    constructor() {
        owner = msg.sender;
    }

    function play(uint8 move) external payable {
        require(msg.value == stake, "Must send 0.0001 tBNB to play");
        require(move >= 1 && move <= 3, "Invalid move");

        Move playerMove = Move(move);
        Move houseMove = Move(randomMove());

        games.push(Game(msg.sender, playerMove, houseMove, false, false));
        pot += msg.value;
    }

    function randomMove() private view returns (uint8) {
        return uint8(uint256(keccak256(abi.encodePacked(block.timestamp, block.difficulty, msg.sender))) % 3) + 1;
    }

    // Implement a function to resolve and reward games

    function getGameCount() public view returns (uint256) {
        return games.length;
    }
}
